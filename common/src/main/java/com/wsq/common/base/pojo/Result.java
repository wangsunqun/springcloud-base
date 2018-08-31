package com.wsq.common.base.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Result<T> {
    private int code;
    private T data;
    private String msg;
}