package edu.mv.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.mv.db.models.Rocket;
import edu.mv.mapping.RocketMapper;
import edu.mv.models.RocketDTO;
import edu.mv.repository.RocketRepository;

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
