package com.wsq.common.base.pojo;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Response extends Result{
    public Response(Object data) {
        super(0, data, "");
    }
}