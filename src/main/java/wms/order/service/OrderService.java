package wms.order.service;

import org.springframework.web.multipart.MultipartFile;
import wms.order.dto.OrderItemDto;
import wms.order.dto.OrderRequestDto;
import wms.order.entity.Order;
import wms.order.entity.OrderItem;
import wms.order.exception.GlobalExceptionHandler;
import wms.order.repository.OrderItemRepository;
import wms.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public void createOrder(OrderRequestDto orderRequestDto) {
        // 1. 주문(Order) 생성
        Order order = new Order();
        order.setVendorId(orderRequestDto.getOrderMaster().getVendorId());
        order.setVendorNm(orderRequestDto.getOrderMaster().getVendorNm());
        order.setChannelId(orderRequestDto.getOrderMaster().getChannelId());
        order.setChannelNm(orderRequestDto.getOrderMaster().getChannelNm());
        order.setChannelOrderId(orderRequestDto.getOrderMaster().getChannelOrderId());
        order.setChannelOrderDate(orderRequestDto.getOrderMaster().getChannelOrderDate());
        order.setChannelOrderUserNm(orderRequestDto.getOrderMaster().getChannelOrderUserNm());
        order.setChannelOrderCurrencyCd(orderRequestDto.getOrderMaster().getChannelOrderCurrencyCd());
        order.setChannelOrderAmount(orderRequestDto.getOrderMaster().getChannelOrderAmount());
        order.setShippingFee(orderRequestDto.getOrderMaster().getShippingFee());

        // 배송 정보 설정
        order.setRecipientNm(orderRequestDto.getDeliveryInfo().getRecipientNm());
        order.setRecipientZipCode(orderRequestDto.getDeliveryInfo().getRecipientZipCode());
        order.setRecipientAddr1(orderRequestDto.getDeliveryInfo().getRecipientAddr1());
        order.setRecipientAddr2(orderRequestDto.getDeliveryInfo().getRecipientAddr2());
        order.setRecipientTelNo(orderRequestDto.getDeliveryInfo().getRecipientTelNo());
        order.setRecipientMobileNo(orderRequestDto.getDeliveryInfo().getRecipientMobileNo());
        order.setRecipientEmail(orderRequestDto.getDeliveryInfo().getRecipientEmail());
        order.setRecipientMemo(orderRequestDto.getDeliveryInfo().getRecipientMemo());

        // 2. Order 저장
        orderRepository.save(order);

        // 3. OrderItem 설정 및 저장
        List<OrderItem> orderItems = orderRequestDto.getOrderItem().stream()
                .map(orderItemDto -> convertToOrderItem(order, orderItemDto))
                .collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);
    }

    // OrderItemDto를 OrderItem 엔티티로 변환하는 메서드
    private OrderItem convertToOrderItem(Order order, OrderItemDto orderItemDto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);  // Order와의 연관 설정
        orderItem.setOrderId(order.getOrderId());  // 복합 키의 일부로 사용

        // orderSeq 설정 로직
        Long maxOrderSeq = orderItemRepository.findMaxOrderSeqByOrderId(order.getOrderId());
        orderItem.setOrderSeq((maxOrderSeq != null) ? maxOrderSeq + 1 : 1L);  // 첫 번째 항목이면 1로 시작

        orderItem.setChannelOrderSeq(orderItemDto.getChannelOrderSeq());
        orderItem.setChannelItemCode(orderItemDto.getChannelItemCode());
        orderItem.setChannelOptionCode(orderItemDto.getChannelOptionCode());
        orderItem.setChannelItemNm(orderItemDto.getChannelItemNm());
        orderItem.setChannelOrderAmt(orderItemDto.getChannelOrderAmt());
        orderItem.setChannelItemUrl(orderItemDto.getChannelItemUrl());
        orderItem.setVendorOrderId(orderItemDto.getVendorOrderId());
        orderItem.setVendorOrderSeq(orderItemDto.getVendorOrderSeq());
        orderItem.setVendorItemCode(orderItemDto.getVendorItemCode());
        orderItem.setVendorOptionCode(orderItemDto.getVendorOptionCode());
        orderItem.setVendorBarcode(orderItemDto.getVendorBarcode());
        orderItem.setVendorItemNm(orderItemDto.getVendorItemNm());
        orderItem.setVendorItemUrl(orderItemDto.getVendorItemUrl());
        orderItem.setUpcCode(orderItemDto.getUpcCode());
        orderItem.setHsCode(orderItemDto.getHsCode());
        orderItem.setOptionTypeNm1(orderItemDto.getOptionTypeNm1());
        orderItem.setOptionNm1(orderItemDto.getOptionNm1());
        orderItem.setOptionTypeNm2(orderItemDto.getOptionTypeNm2());
        orderItem.setOptionNm2(orderItemDto.getOptionNm2());
        orderItem.setItemImgUrl(orderItemDto.getItemImgUrl());
        orderItem.setOriginNationCd(orderItemDto.getOriginNationCd());
        orderItem.setMaterial(orderItemDto.getMaterial());
        orderItem.setWeight(orderItemDto.getWeight());
        orderItem.setWeightUnit(orderItemDto.getWeightUnit());
        orderItem.setOrderQty(orderItemDto.getOrderQty());

        return orderItem;
    }

    // 주문 ID로 특정 주문 조회
    public List<Order> getOrderById(String orderId) {
        List<Order> orders = orderRepository.findByOrderId(orderId);

        if (orders.isEmpty()) {
            throw new GlobalExceptionHandler.OrderNotFoundException("Order ID: " + orderId);
        }

        return orders;
    }

    // 벤더 ID로 특정 주문 조회
    public List<Order> getOrderByVendor(String vendorId) {
        List<Order> orders = orderRepository.findByVendorId(vendorId);

        if (orders.isEmpty()) {
            throw new GlobalExceptionHandler.OrderNotFoundException("Order ID: " + vendorId);
        }

        return orders;
    }

    // 엑셀 파일을 처리
    public void processExcelFile(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            List<Order> orders = new ArrayList<>();

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                // 각 셀에서 데이터를 읽어 Order 및 OrderItem 객체를 생성합니다.
                Order order = new Order();
                order.setVendorId(getCellValue(row, 0));  // 예: 0번 열에 vendorId가 있다고 가정
                order.setVendorNm(getCellValue(row, 1));  // 예: 1번 열에 vendorNm
                order.setChannelId(getCellValue(row, 2));
                order.setChannelNm(getCellValue(row, 3));
                order.setChannelOrderId(getCellValue(row, 4));
                order.setChannelOrderDate(getCellValue(row, 5));
                order.setChannelOrderUserNm(getCellValue(row, 6));
                order.setChannelOrderCurrencyCd(getCellValue(row, 7));
                order.setChannelOrderAmount(Double.parseDouble(getCellValue(row, 8)));
                order.setShippingFee(Double.parseDouble(getCellValue(row, 9)));
                order.setRecipientNm(getCellValue(row, 10));
                order.setRecipientZipCode(getCellValue(row, 11));
                order.setRecipientAddr1(getCellValue(row, 12));
                order.setRecipientAddr2(getCellValue(row, 13));
                order.setRecipientTelNo(getCellValue(row, 14));
                order.setRecipientMobileNo(getCellValue(row, 15));
                order.setRecipientEmail(getCellValue(row, 16));
                order.setRecipientMemo(getCellValue(row, 17));

                // Order 저장
                orderRepository.save(order);

                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(order.getOrderId());

                // orderSeq 설정 로직
                Long maxOrderSeq = orderItemRepository.findMaxOrderSeqByOrderId(order.getOrderId());
                orderItem.setOrderSeq((maxOrderSeq != null) ? maxOrderSeq + 1 : 1L);  // 첫 번째 항목이면 1로 시작
                orderItem.setChannelOrderSeq(getCellValue(row, 18));  // 18번 열: 채널 주문 시퀀스
                orderItem.setChannelItemCode(getCellValue(row, 19));  // 19번 열: 채널 상품 코드
                orderItem.setChannelOptionCode(getCellValue(row, 20));  // 20번 열: 채널 옵션 코드
                orderItem.setChannelItemNm(getCellValue(row, 21));  // 21번 열: 채널 상품 이름
                orderItem.setChannelOrderAmt(Double.parseDouble(getCellValue(row, 22)));  // 22번 열: 채널 주문 금액
                orderItem.setChannelItemUrl(getCellValue(row, 23));  // 23번 열: 채널 상품 URL
                orderItem.setVendorOrderId(getCellValue(row, 24));  // 24번 열: 벤더 주문 ID
                orderItem.setVendorOrderSeq(getCellValue(row, 25));  // 25번 열: 벤더 주문 시퀀스
                orderItem.setVendorItemCode(getCellValue(row, 26));  // 26번 열: 벤더 상품 코드
                orderItem.setVendorOptionCode(getCellValue(row, 27));  // 27번 열: 벤더 옵션 코드
                orderItem.setVendorBarcode(getCellValue(row, 28));  // 28번 열: 벤더 바코드
                orderItem.setVendorItemNm(getCellValue(row, 29));  // 29번 열: 벤더 상품 이름
                orderItem.setVendorItemUrl(getCellValue(row, 30));  // 30번 열: 벤더 상품 URL
                orderItem.setUpcCode(getCellValue(row, 31));  // 31번 열: UPC 코드
                orderItem.setHsCode(getCellValue(row, 32));  // 32번 열: HS 코드
                orderItem.setOptionTypeNm1(getCellValue(row, 33));  // 33번 열: 옵션 유형 1
                orderItem.setOptionNm1(getCellValue(row, 34));  // 34번 열: 옵션 이름 1
                orderItem.setOptionTypeNm2(getCellValue(row, 35));  // 35번 열: 옵션 유형 2
                orderItem.setOptionNm2(getCellValue(row, 36));  // 36번 열: 옵션 이름 2
                orderItem.setOptionTypeNm3(getCellValue(row, 37));  // 37번 열: 옵션 유형 3
                orderItem.setOptionNm3(getCellValue(row, 38));  // 38번 열: 옵션 이름 3
                orderItem.setOptionTypeNm4(getCellValue(row, 39));  // 39번 열: 옵션 유형 4
                orderItem.setOptionNm4(getCellValue(row, 40));  // 40번 열: 옵션 이름 4
                orderItem.setOptionTypeNm5(getCellValue(row, 41));  // 41번 열: 옵션 유형 5
                orderItem.setOptionNm5(getCellValue(row, 42));  // 42번 열: 옵션 이름 5
                orderItem.setItemImgUrl(getCellValue(row, 43));  // 43번 열: 상품 이미지 URL
                orderItem.setItemOrderUrl(getCellValue(row, 44));  // 44번 열: 상품 주문 URL
                orderItem.setOriginNationCd(getCellValue(row, 45));  // 45번 열: 원산지 코드
                orderItem.setMaterial(getCellValue(row, 46));  // 46번 열: 소재
                orderItem.setWeight(Double.parseDouble(getCellValue(row, 47)));  // 47번 열: 무게
                orderItem.setWeightUnit(getCellValue(row, 48));  // 48번 열: 무게 단위
                orderItem.setOrderQty(Integer.parseInt(getCellValue(row, 49)));  // 49번 열: 주문 수량

                // OrderItem 저장
                orderItemRepository.save(orderItem);

                orders.add(order);
            }
        } catch (IOException e) {
            throw new GlobalExceptionHandler.FileProcessingException("엑셀 파일을 처리하는 중 오류가 발생했습니다.", e);
        }
    }

    // 셀 값을 문자열로 가져오는 유틸리티 메서드
    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((int) cell.getNumericCellValue());
        } else {
            return "";
        }
    }

    // 엑셀 양식 생성 메서드
    public Workbook createExcelTemplate() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Order Template");

        // 첫 번째 행(헤더) 생성
        Row headerRow = sheet.createRow(0);

        // 컬럼 헤더 생성
        String[] headers = {"Vendor ID", "Vendor Name", "Channel ID", "Channel Name", "Channel Order ID",
                "Channel Order Date", "Channel Order User Name", "Currency Code",
                "Order Amount", "Shipping Fee", "Recipient Name", "Zip Code",
                "Address Line 1", "Address Line 2", "Telephone", "Mobile", "Email",
                "Memo", "Channel Order Seq", "Channel Item Code", "Channel Item Name",
                "Channel Order Amount", "Vendor Item Code"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // 자동 열 너비 조정
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }
}