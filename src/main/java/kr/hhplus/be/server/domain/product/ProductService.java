package kr.hhplus.be.server.domain.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public Product getProductOrThrow(long productId) {
        return productRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. productId=" + productId));
    }

    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product decreaseStock(long productId, int decreaseQuantity) {
        Product product = getProductOrThrow(productId);
        product.decreaseStock(decreaseQuantity);

        return productRepository.save(product);
    }

    public Product increaseStock(long productId, int increaseQuantity) {
        Product product = getProductOrThrow(productId);
        product.increaseStock(increaseQuantity);

        return productRepository.save(product);
    }

    public int getStock(Long productId) {
        Product product = getProductOrThrow(productId);
        return product.getStock();
    }

    public void checkStock(long productId, int requiredQuantity) {
        Product product = getProductOrThrow(productId);
        if (product.getStock() < requiredQuantity) {
            throw new IllegalStateException("상품 재고가 부족합니다.");
        }
    }
}