package io.github.mateusferian.quarkussocial.domains.repositories;

import io.github.mateusferian.quarkussocial.domains.models.PostModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostRepository implements PanacheRepository<PostModel> {
}
