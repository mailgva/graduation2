package com.voting.service;

import com.voting.model.Resto;
import com.voting.model.User;
import com.voting.model.Vote;
import com.voting.util.exception.PastDateException;
import com.voting.util.exception.TooLateEcxeption;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ActiveProfiles("datajpa")
public class VoteServiceTest extends AbstractServiceTest{

    @Autowired
    private VoteService service;

    @Autowired
    private UserService userService;

    @Autowired
    private RestoService restoService;


    @Test
    public void create() throws ParseException {
        User user = userService.get(100001);
        Resto resto = restoService.get(100003);
        LocalDate ld = LocalDate.now().plusDays(1);

        Calendar calendar = new GregorianCalendar(ld.getYear(), ld.getMonthValue(), ld.getDayOfMonth());
        Date date = calendar.getTime();
        Vote vote = new Vote(null, user, resto, date, LocalDateTime.now());
        service.create(vote, 100001);
    }

    @Test //(expected = TooLateEcxeption.class)
    public void update() {
        assertThrows(PastDateException.class, () -> {

            User user = userService.get(100001);
            Resto resto = restoService.get(100003);

            Calendar calendar = new GregorianCalendar(2018,Calendar.NOVEMBER,07);
            Date date = calendar.getTime();

            Vote vote = new Vote(null, user, resto, date, LocalDateTime.now());
            service.create(vote, 100001);

            Vote newVote = service.get(100127, 100001);
            newVote.setResto(restoService.get(100004));
            calendar = new GregorianCalendar(2018,Calendar.NOVEMBER,01);
            date = calendar.getTime();
            newVote.setDate(date);
            service.update(newVote, 100001);
        });
    }

    @Test
    public void get() {
        System.out.println(service.get(100030,100001));

    }

    @Test
    public void delete() {
        service.delete(100060, 100001);
    }

    @Test
    public void getBetweenDates() {
    }

    @Test
    public void getBetweenDateTimes() {
    }

    @Test
    public void getAll() {
        assertEquals(service.getAll(100001).size(), 8);
    }


    @Test
    public void getByDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2018-11-11");
        Vote vote = service.getByDate(date, 100000);
        System.out.println(vote);
    }

    @Test //(expected = Exception.class)
    public void createDublicat() {
        assertThrows(PastDateException.class, () -> {

            User user = userService.get(100001);
            Resto resto = restoService.get(100003);

            Calendar calendar = new GregorianCalendar(2018, Calendar.NOVEMBER, 30);
            Date date = calendar.getTime();

            Vote vote = new Vote(null, user, resto, date, LocalDateTime.now());
            Vote newVote = service.create(vote, 100001);

            newVote.setResto(restoService.get(100004));
            newVote.setId(null);

            service.update(newVote, 100001);

            newVote.setId(null);
            service.create(newVote, 100001);
        });
    }

}