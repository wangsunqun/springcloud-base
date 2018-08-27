package com.wsq.zuul.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RateLimit {
    private int count;
    private int time;
}
