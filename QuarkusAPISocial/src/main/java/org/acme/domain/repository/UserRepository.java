package org.acme.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.model.User;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<User, Long> {

}
