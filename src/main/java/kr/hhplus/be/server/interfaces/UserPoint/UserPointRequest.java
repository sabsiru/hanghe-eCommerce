package kr.hhplus.be.server.interfaces.UserPoint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserPointRequest {
    private Long userId;
    private int chargeAmount;
}
