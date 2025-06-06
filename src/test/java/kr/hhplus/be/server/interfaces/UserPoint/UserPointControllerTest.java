package kr.hhplus.be.server.interfaces.UserPoint;

import kr.hhplus.be.server.application.user.UserPointFacade;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserPointController.class)
public class UserPointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserPointFacade userPointFacade;

    @Test
    void 정상_충전() throws Exception {
        // given
        Long userId = 1L;
        int chargeAmount = 5000;
        User updatedUser = new User(userId, "테스터", 15000, LocalDateTime.now().minusDays(1), LocalDateTime.now());
        when(userPointFacade.chargePoint(eq(userId), eq(chargeAmount))).thenReturn(updatedUser);

        // when & then
        String requestBody = "{\"userId\": 1, \"chargeAmount\": 5000}";

        mockMvc.perform(post("/point/1/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("테스터"))
                .andExpect(jsonPath("$.point").value(updatedUser.getPoint()));
    }
    @Test
    public void 컨트롤러_포인트충전_최대한도_초과() throws Exception{
        //given
        long userId = 1L;
        int chargeAmount = 2;
        int current = 9_999_999;
        User user = new User(userId, "테스터", current, LocalDateTime.now().minusDays(1), LocalDateTime.now());
        when(userPointFacade.chargePoint(eq(userId), eq(chargeAmount))).thenThrow(new IllegalArgumentException("최대 충전 한도는 `10,000,000원` 입니다."));
        //when
        String requestBody = "{\"userId\": 1, \"chargeAmount\": " + chargeAmount + "}";


        //then
        // when & then
        mockMvc.perform(post("/point/1/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("최대 충전 한도는 `10,000,000원` 입니다.")));
    }

        @Test
        void 충전_1회한도_초과() throws Exception {
            // given
            Long userId = 1L;
            int excessiveChargeAmount = 1500000; // 1,500,000원
            when(userPointFacade.chargePoint(eq(userId), eq(excessiveChargeAmount)))
                    .thenThrow(new IllegalArgumentException("1회 충전 금액은 `1,000,000원` 입니다."));

            String requestBody = "{\"userId\": 1, \"chargeAmount\": " + excessiveChargeAmount + "}";

            // when & t
            mockMvc.perform(post("/point/1/charge")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("1회 충전 금액은 `1,000,000원` 입니다.")));
        }
}