package main.jdbc.mysql;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by fansy on 2017/12/20.
 */
// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Long> {

}
