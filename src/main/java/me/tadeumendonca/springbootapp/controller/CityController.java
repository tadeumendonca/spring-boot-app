package me.tadeumendonca.springbootapp.controller;
import me.tadeumendonca.springbootapp.domain.City;
import me.tadeumendonca.springbootapp.exception.ResourceNotFoundException;
import me.tadeumendonca.springbootapp.exception.ResourceAlreadyExistsException;
import me.tadeumendonca.springbootapp.exception.BadResourceException;
import me.tadeumendonca.springbootapp.service.CityService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final int ROW_PER_PAGE = 5;

    @Autowired
    private CityService cityService;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<City>> findAll(
            @RequestParam(value="page", defaultValue="1") int pageNumber,
            @RequestParam(required=false) String name) {
        if (StringUtils.isEmpty(name)) {
            return ResponseEntity.ok(cityService.findAll(pageNumber, ROW_PER_PAGE));
        }
        else {
            return ResponseEntity.ok(cityService.findAllByName(name, pageNumber, ROW_PER_PAGE));
        }
    }

    @GetMapping(value = "/{cityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<City> findCityById(@PathVariable long cityId) {
        try {
            City city = cityService.findById(cityId);
            return ResponseEntity.ok(city);  // return 200, with json body
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // return 404, with null body
        }
    }

    @PostMapping(value = "/")
    public ResponseEntity<City> addCity(@Valid @RequestBody City city)
            throws URISyntaxException {
        try {
            City newCity = cityService.save(city);
            return ResponseEntity.created(new URI("/api/cities/" + newCity.getId()))
                    .body(newCity);
        } catch (ResourceAlreadyExistsException ex) {
            // log exception first, then return Conflict (409)
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (BadResourceException ex) {
            // log exception first, then return Bad Request (400)
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping(value = "/{cityId}")
    public ResponseEntity<City> updateCity(@Valid @RequestBody City city,
                                                 @PathVariable long cityId) {
        try {
            city.setId(cityId);
            cityService.update(city);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            // log exception first, then return Not Found (404)
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (BadResourceException ex) {
            // log exception first, then return Bad Request (400)
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(path="/{cityId}")
    public ResponseEntity<Void> deleteCityById(@PathVariable long cityId) {
        try {
            cityService.deleteById(cityId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
