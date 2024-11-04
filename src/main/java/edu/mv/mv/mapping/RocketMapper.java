package edu.mv.mv.mapping;

import edu.mv.mv.db.models.Rocket;
import edu.mv.mv.models.RocketDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RocketMapper {

    RocketMapper INSTANCE = Mappers.getMapper(RocketMapper.class);

    @Mapping(source = "sorte", target = "type")
    RocketDTO RocketToRocketDTO(Rocket Rocket);

    @Mapping(source = "type", target = "sorte")
    Rocket RocketDTOToRocket(RocketDTO RocketDTO);

}
