package org.acme.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.entities.Follower;
import org.acme.domain.entities.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class FollowersRepository implements PanacheRepository<Follower> {

    public boolean isFollowing(User follower , User user) {
        /*Map<String,Object> params = new HashMap<>();
        params.put("follower",follower);
        params.put("user",user);
        find("follower =:follower and user =:user", params);*/
        var params = Parameters.with("follower",follower).and("user",user).map();
        PanacheQuery<Follower> query = find("follower =:follower and user =:user", params);
        Optional<Follower> followerOptional = query.firstResultOptional();
        return followerOptional.isPresent();
   }

   public List<Follower> findByUser(Long userId) {
       PanacheQuery<Follower> query = find("user.id", userId);
         return query.list();
    }

    public void deleteByFollowerAndUser(Long followerId, Long userId) {
        var params = Parameters.with("userId",userId).and("followerId",followerId).map();

        delete("follower.id =:followerId and user.id =:userId", params);
    }
}
