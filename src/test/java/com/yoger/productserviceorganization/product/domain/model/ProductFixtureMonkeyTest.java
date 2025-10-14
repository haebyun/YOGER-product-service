package com.yoger.productserviceorganization.product.domain.model;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.yoger.productserviceorganization.product.domain.exception.InsufficientStockException;
import com.yoger.productserviceorganization.product.domain.exception.InvalidProductException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ProductFixtureMonkeyTest {

    private final FixtureMonkey sut = FixtureMonkey.builder()
            .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
            .register(Stock.class, builder -> builder.giveMeBuilder(Stock.class)
                    .set("stockQuantity", 10))
            .build();

    @Test
    void 판매_가능한_상품은_재고를_차감할_수_있다() {
        Product product = sut.giveMeBuilder(Product.class)
                .set("state", ProductState.SELLABLE)
                .sample();

        product.deductStockQuantity(10);

        assertThat(product.getStockQuantity()).isEqualTo(0);
    }

    @Test
    void 판매_완료된_상품은_재고_차감_시_예외가_발생한다() {
        Product product = sut.giveMeBuilder(Product.class)
                .set("state", ProductState.SALE_ENDED)
                .sample();

        assertThatThrownBy(() -> product.deductStockQuantity(1))
                .isInstanceOf(InvalidProductException.class)
                .hasMessage("상품이 예상된 상태가 아닙니다.");
    }

    @Test
    void 재고보다_많이_차감하면_예외가_발생한다() {

        // Stock 객체를 Product 내부 필드에 주입
        Product product = sut.giveMeBuilder(Product.class)
                .set("state", ProductState.SELLABLE)
                .sample();

        System.out.println("재고: " + product.getStockQuantity());

        // 예상된 예외 검증
        assertThatThrownBy(() -> product.deductStockQuantity(15))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessage("재고 수량이 부족합니다.");
    }

    @Test
    void 판매가능상품에서_판매완료상품으로_변경할_수_있다() {
        Product sellableProduct = sut.giveMeBuilder(Product.class)
                .set("state", ProductState.SELLABLE)
                .sample();

        Product ended = Product.toSaleEndedFrom(sellableProduct);

        assertThat(ended.getState()).isEqualTo(ProductState.SALE_ENDED);
    }

    @Test
    void 판매가능상태가_아니면_toSaleEndedFrom_호출시_예외발생() {
        Product wrongStateProduct = sut.giveMeBuilder(Product.class)
                .set("state", ProductState.SALE_ENDED)
                .sample();

        assertThatThrownBy(() -> Product.toSaleEndedFrom(wrongStateProduct))
                .isInstanceOf(InvalidProductException.class);
    }
}
