package ie.nuig.i3market.semantic.engine;

import ie.nuig.i3market.semantic.engine.repository.OfferingRepository;
import ie.nuig.i3market.semantic.engine.service.DataOfferingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OfferingServiceUnitTest {
    @InjectMocks
    private DataOfferingService service;

    @Mock
    private OfferingRepository repository;

    @Test
    public void getTotalOfferingByProviderId() {
        Assertions.fail();
    }
}
