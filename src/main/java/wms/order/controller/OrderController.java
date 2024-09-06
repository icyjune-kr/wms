package wms.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import wms.order.dto.CommonResponseDto;
import wms.order.dto.OrderRequestDto;
import wms.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.order.entity.Order;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;

import javax.validation.Valid;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/view")
    public String orderForm() {
        return "index";  // src/main/resources/templates/index.html을 반환
    }

    @Operation(summary = "단일 주문 생성", description = "주문 데이터를 전달받아 단일 주문을 생성합니다.")
    @PostMapping
    public ResponseEntity<CommonResponseDto> createOrder(
            @Valid @RequestBody OrderRequestDto orderRequestDto) {
        orderService.createOrder(orderRequestDto);
        return ResponseEntity.ok(CommonResponseDto.success());
    }

    @Operation(summary = "주문 ID로 주문 조회", description = "주문 ID로 주문을 조회")
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @Parameter(description = "조회할 주문의 ID", example = "ORD12345") @PathVariable String orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "공급처 ID로 주문 조회", description = "공급처 ID로 주문을 조회")
    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<Order> getOrderByVendor(
            @Parameter(description = "조회할 벤더의 ID", example = "VEN12345") @PathVariable String vendorId) {
        Order order = orderService.getOrderByVendor(vendorId);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "엑셀 파일로 주문 생성", description = "엑셀 파일 업로드")
    @PostMapping("/excelUpload")
    public ResponseEntity<CommonResponseDto> uploadOrders(
            @Parameter(description = "업로드할 엑셀 파일") @RequestParam("file") MultipartFile file) {
        try {
            orderService.processExcelFile(file);
            return ResponseEntity.ok(CommonResponseDto.success());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(CommonResponseDto.error("99", "엑셀 파일 처리 중 오류가 발생했습니다."));
        }
    }

    // 엑셀 양식 다운로드
    @GetMapping("/download-template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        Workbook workbook = orderService.createExcelTemplate();  // 엑셀 양식 생성
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        byte[] excelBytes = out.toByteArray();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order_template.xlsx");
        response.getOutputStream().write(excelBytes);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
}
