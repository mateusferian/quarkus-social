package io.github.mateusferian.quarkussocial.domains.repositories;

import io.github.mateusferian.quarkussocial.domains.models.FollowerModel;
import io.github.mateusferian.quarkussocial.domains.models.UserModel;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<FollowerModel> {

    public boolean followers(UserModel follower, UserModel user){
        var params = Parameters
                .with("follower", follower)
                .and("user",user)
                .map();

        PanacheQuery<FollowerModel> query = find("follower = :follower and user = :user", params);
        Optional<FollowerModel> result = query.firstResultOptional();

        return result.isPresent();
    }

    public List<FollowerModel> findByUser(Long userId){
        PanacheQuery<FollowerModel> query = find("user.id", userId);
        return query.list();
    }

    public void deleteByFollowerAndUser(Long followerId, Long userId) {
        var params = Parameters
                .with("userId",userId)
                .and("followerId", followerId)
                .map();

        delete("follower.id = :followerId and user.id = :userId",params);
    }
}
