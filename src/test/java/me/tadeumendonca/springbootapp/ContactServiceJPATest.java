package me.tadeumendonca.springbootapp;
import me.tadeumendonca.springbootapp.domain.Contact;
import me.tadeumendonca.springbootapp.exception.ResourceNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import javax.sql.DataSource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import me.tadeumendonca.springbootapp.service.ContactService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContactServiceJPATest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ContactService contactService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testSaveUpdateDeleteContact() throws Exception{
        Contact c = new Contact();
        c.setName("Portgas D. Ace");

        contactService.save(c);
        assertNotNull(c.getId());

        Contact findContact = contactService.findById(c.getId());
        assertEquals("Portgas D. Ace", findContact.getName());

        // update record
        c.setName("Portgas D. Ace 2");
        contactService.update(c);

        // test after update
        findContact = contactService.findById(c.getId());
        assertEquals("Portgas D. Ace 2", findContact.getName());

        // test delete
        contactService.deleteById(c.getId());

        // query after delete
        exceptionRule.expect(ResourceNotFoundException.class);
        contactService.findById(c.getId());
    }
}
