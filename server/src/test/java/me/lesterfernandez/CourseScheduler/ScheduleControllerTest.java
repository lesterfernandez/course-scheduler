package me.lesterfernandez.CourseScheduler;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.lesterfernandez.CourseScheduler.auth.AuthContext;
import me.lesterfernandez.CourseScheduler.course.CourseDto;
import me.lesterfernandez.CourseScheduler.schedule.ScheduleDto;
import me.lesterfernandez.CourseScheduler.user.UserEntity;
import me.lesterfernandez.CourseScheduler.user.UserService;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ScheduleControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private AuthContext authContext;

  @MockBean
  private UserService userService;

  @Test
  public void shouldSortSchedule() throws Exception {
    UserEntity user = new UserEntity("lester", "lester");
    authContext.authorized = true;
    when(authContext.getUsername()).thenReturn(user.getUsername());
    when(userService.findByUsername("lester")).thenReturn(user);

    CourseDto algebra = CourseDto.builder().courseIndex(-1).letters("MAC").number("1105").uuid("1").build();
    CourseDto chem1045 = CourseDto.builder().courseIndex(-1).letters("CHM").number("1045").uuid("6").build();
    CourseDto calc1 = CourseDto.builder().courseIndex(-1).letters("MAC").number("2311").uuid("2").build();
    CourseDto calc2 = CourseDto.builder().courseIndex(-1).letters("MAC").number("2312").uuid("3").build();
    CourseDto physics1 = CourseDto.builder().courseIndex(-1).letters("PHY").number("2048").uuid("4").build();
    CourseDto physics2 = CourseDto.builder().courseIndex(-1).letters("PHY").number("2049").uuid("5").build();
    chem1045.getPrerequisites().add(algebra.getUuid());
    calc1.getPrerequisites().add(algebra.getUuid());
    physics1.getPrerequisites().add(calc1.getUuid());
    calc2.getPrerequisites().add(calc1.getUuid());
    physics2.getPrerequisites().add(chem1045.getUuid());
    physics2.getPrerequisites().add(calc2.getUuid());

    // random ordering
    ScheduleDto schedule = new ScheduleDto(List.of(calc2, physics2, chem1045, calc1, algebra, physics1));

    ResultActions response = mockMvc.perform(post("/api/schedule")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(schedule)));

    response.andExpect(MockMvcResultMatchers.status().isCreated());
  }

}
