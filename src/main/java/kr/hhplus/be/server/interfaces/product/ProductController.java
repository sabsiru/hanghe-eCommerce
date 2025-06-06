package kr.hhplus.be.server.interfaces.product;

import kr.hhplus.be.server.application.product.PopularProductInfo;
import kr.hhplus.be.server.application.product.ProductFacade;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.infrastructure.product.ProductSummaryRow;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductFacade productFacade;

    @GetMapping("/latest")
    public List<ProductSummaryRow> getLatestProducts(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int limit
    ) {
        return productFacade.getLatestProducts(offset, limit);
    }

    @GetMapping("/latest-cursor")
    public List<ProductResponse> getLatestProductsWithCursor(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursorCreatedAt,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "20") int limit
    ) {
        return productFacade.getLatestProductsCursor(cursorCreatedAt, cursorId, limit)
                .stream()
                .map(ProductResponse::from)
                .toList();
    }

    @GetMapping("/popular")
    public List<PopularProductInfo> getPopularProducts() {
        return productFacade.getPopularProducts();
    }
}
