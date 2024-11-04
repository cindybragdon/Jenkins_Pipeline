package edu.mv.mv.persistence;

import java.util.Optional;

import edu.mv.mv.repository.RocketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.mv.mv.db.models.Rocket;
import edu.mv.mv.mapping.RocketMapper;
import edu.mv.mv.models.RocketDTO;

@Service
public class PersistenceService {

    @Autowired
    private RocketRepository rocketRepository;

    public RocketDTO retrieve(int id) throws RocketNotFoundException {
        Optional<Rocket> rocketOptional = rocketRepository.findById(id);
        System.out.println(rocketOptional.toString());
        if (rocketOptional.isPresent()) {
            return convertToRocketDTO(rocketOptional.get());
        }

        throw new RocketNotFoundException(id);
    }

    public void save(RocketDTO Rocket) {
        rocketRepository.save(convertToRocketPersistence(Rocket));
    }

    private Rocket convertToRocketPersistence(RocketDTO RocketDTO) {
        Rocket rocket = RocketMapper.INSTANCE.RocketDTOToRocket(RocketDTO);
        return rocket;
    }

    private RocketDTO convertToRocketDTO(Rocket rocket) {
        RocketDTO rocketDTO = RocketMapper.INSTANCE.RocketToRocketDTO(rocket);
        return rocketDTO;
    }

}
