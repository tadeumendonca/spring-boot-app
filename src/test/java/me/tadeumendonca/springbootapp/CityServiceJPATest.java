package me.tadeumendonca.springbootapp;

import me.tadeumendonca.springbootapp.domain.City;
import me.tadeumendonca.springbootapp.exception.ResourceNotFoundException;
import me.tadeumendonca.springbootapp.service.CityService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CityServiceJPATest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CityService cityService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testSaveUpdateDeleteContact() throws Exception{
        City c = new City();
        c.setName("Portgas D. Ace");

        cityService.save(c);
        assertNotNull(c.getId());

        City findCity = cityService.findById(c.getId());
        assertEquals("Portgas D. Ace", findCity.getName());

        // update record
        c.setName("Portgas D. Ace 2");
        cityService.update(c);

        // test after update
        findCity = cityService.findById(c.getId());
        assertEquals("Portgas D. Ace 2", findCity.getName());

        // test delete
        cityService.deleteById(c.getId());

        // query after delete
        exceptionRule.expect(ResourceNotFoundException.class);
        cityService.findById(c.getId());
    }
}
