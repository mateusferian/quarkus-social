package io.github.mateusferian.quarkussocial.domains.repositories;

import io.github.mateusferian.quarkussocial.domains.models.UserModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<UserModel> {
}
