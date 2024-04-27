package org.acme.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.model.Post;

@ApplicationScoped
public class PostRepository implements PanacheRepositoryBase<Post, Long> {
}
