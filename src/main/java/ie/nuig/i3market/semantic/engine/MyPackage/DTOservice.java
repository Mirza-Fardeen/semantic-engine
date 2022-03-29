package ie.nuig.i3market.semantic.engine.MyPackage;

import ie.nuig.i3market.semantic.engine.domain.DataProvider;
import ie.nuig.i3market.semantic.engine.domain.optimise.Offering;
import ie.nuig.i3market.semantic.engine.dto.Offerings;
import ie.nuig.i3market.semantic.engine.exceptions.BindingException;
import ie.nuig.i3market.semantic.engine.repository.OfferingRepository;
import ie.nuig.i3market.semantic.engine.service.DataOfferingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class DTOservice {


    @Autowired
    private OfferingRepository offeringRepository;


    public void saveDataProvider(DataProvider dataProvider) throws BindingException, ClassNotFoundException {
        var existingProvider = offeringRepository.getProviderById(dataProvider.getProviderId(), createPageable(0, 1));

        if(existingProvider.size() != 0) {
            throw new DuplicateKeyException("The data providerId '" + dataProvider.getProviderId() + "' is already existed.");
        }
        offeringRepository.saveTemplate(dataProvider);
    }
    private Pageable createPageable(int page, int size) {
        return new Pageable() {
            @Override
            public int getPageNumber() {
                return page;
            }

            @Override
            public int getPageSize() {
                return size;
            }

            @Override
            public long getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public Pageable withPage(int i) {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }
        };
    }
}

