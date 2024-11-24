package com.yoger.productserviceorganization.priceOffer.application;


import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.yoger.productserviceorganization.priceOffer.adapters.web.dto.request.PriceOfferRequestDTO;
import com.yoger.productserviceorganization.priceOffer.domain.exception.PriceOfferAlreadyExistException;
import com.yoger.productserviceorganization.priceOffer.domain.model.PriceOffer;
import com.yoger.productserviceorganization.priceOffer.domain.model.PriceOfferState;
import com.yoger.productserviceorganization.priceOffer.domain.port.PriceOfferRepository;
import com.yoger.productserviceorganization.product.domain.model.PriceByQuantity;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PriceOfferServiceImplTest {

    @InjectMocks
    private PriceOfferServiceImpl priceOfferService;

    @Mock
    private PriceOfferRepository priceOfferRepository;


    Long productId = 1L;
    Long companyId = 2L;
    List<PriceByQuantity> priceByQuantities = List.of(new PriceByQuantity(1, 1000));
    PriceOfferRequestDTO priceOfferRequestDTO = new PriceOfferRequestDTO(priceByQuantities);


    @Test
    void create_success() {
        given(priceOfferRepository.findById(productId, companyId)).willReturn(Optional.empty());
        willDoNothing().given(priceOfferRepository).save(any(PriceOffer.class));

        priceOfferService.create(productId, companyId, priceOfferRequestDTO);

        verify(priceOfferRepository, times(1)).save(any(PriceOffer.class));
    }

    @Test
    void create_failure() {
        PriceOffer priceOffer = PriceOffer.of(productId, companyId, priceByQuantities, PriceOfferState.TEMPORARY);
        Optional<PriceOffer> optionalPriceOffer = Optional.of(priceOffer);
        given(priceOfferRepository.findById(productId, companyId)).willReturn(optionalPriceOffer);

        assertThatThrownBy(() -> priceOfferService.create(productId, companyId, priceOfferRequestDTO))
                .isInstanceOf(PriceOfferAlreadyExistException.class);
    }


}