package edu.mv.mv.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import edu.mv.mv.db.models.Rocket;


public interface RocketRepository extends CrudRepository<Rocket, Integer> {


}
