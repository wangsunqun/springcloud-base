package com.wsq.zuul.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RateLimite {
    private int count;
    private int time;
}
