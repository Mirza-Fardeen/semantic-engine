package ie.nuig.i3market.semantic.engine.service;

import ie.nuig.i3market.semantic.engine.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.function.Function;

public class FluxGeneric<T> {

    private final Scheduler rdfScheduler;

    @Autowired
    public FluxGeneric(@Qualifier("rdfScheduler") Scheduler rdfScheduler) {
        this.rdfScheduler = rdfScheduler;
    }

    public Flux<T> fluxList (List<T> offerings, String message) {
        return Mono.fromCallable(() -> offerings)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, message)))
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(rdfScheduler);
    }

    public Function<List<T>, List<T>> getList
            =(a) -> {
        if (a.size() == 0) {
            return null;
        }
        return a;
    };
}
