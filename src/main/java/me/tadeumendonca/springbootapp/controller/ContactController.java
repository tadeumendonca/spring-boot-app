package me.tadeumendonca.springbootapp.controller;

import me.tadeumendonca.springbootapp.domain.Contact;
import me.tadeumendonca.springbootapp.exception.BadResourceException;
import me.tadeumendonca.springbootapp.exception.ResourceAlreadyExistsException;
import me.tadeumendonca.springbootapp.exception.ResourceNotFoundException;
import me.tadeumendonca.springbootapp.service.CityService;
import me.tadeumendonca.springbootapp.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ContactController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final int ROW_PER_PAGE = 5;

    @Autowired
    private ContactService contactService;

    @GetMapping(value = "/contacts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Contact>> findAll(
            @RequestParam(value="page", defaultValue="1") int pageNumber,
            @RequestParam(required=false) String name) {
        if (StringUtils.isEmpty(name)) {
            return ResponseEntity.ok(contactService.findAll(pageNumber, ROW_PER_PAGE));
        }
        else {
            return ResponseEntity.ok(contactService.findAllByName(name, pageNumber, ROW_PER_PAGE));
        }
    }

    @GetMapping(value = "/contacts/{contactId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Contact> findCityById(@PathVariable long contactId) {
        try {
            Contact contact = contactService.findById(contactId);
            return ResponseEntity.ok(contact);  // return 200, with json body
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // return 404, with null body
        }
    }

    @PostMapping(value = "/contacts")
    public ResponseEntity<Contact> addCity(@Valid @RequestBody Contact contact)
            throws URISyntaxException {
        try {
            Contact newContact = contactService.save(contact);
            return ResponseEntity.created(new URI("/api/contacts/" + newContact.getId()))
                    .body(newContact);
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

    @PutMapping(value = "/contacts/{contactId}")
    public ResponseEntity<Contact> updateCity(@Valid @RequestBody Contact contact,
                                                 @PathVariable long contactId) {
        try {
            contact.setId(contactId);
            contactService.update(contact);
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

    @DeleteMapping(path="/contacts/{contactId}")
    public ResponseEntity<Void> deleteCityById(@PathVariable long contactId) {
        try {
            contactService.deleteById(contactId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
