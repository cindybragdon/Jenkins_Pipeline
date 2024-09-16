package edu.mv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.mv.models.RocketDTO;
import edu.mv.persistence.RocketNotFoundException;
import edu.mv.service.RocketService;

@RestController
@RequestMapping("/")
public class RocketController {

    @Autowired
    private RocketService rocketService;


    public RocketController(RocketService rocketService) {
        this.rocketService = rocketService;
    }

    @GetMapping("/rocket/{rocketid}")
    public RocketDTO getRocket(@PathVariable(value = "rocketid") final String id) throws GetRocketException {
        try {
            return rocketService.getRocket(Integer.parseInt(id));
        } catch (RocketNotFoundException e) {
            throw new GetRocketException();
        }
    }

    @PostMapping("/rocket")
    public void saveRocket(@RequestBody RocketDTO rocket) {
        rocketService.putRocket(rocket);
    }
}
