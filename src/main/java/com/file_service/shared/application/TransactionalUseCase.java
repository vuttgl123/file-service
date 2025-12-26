package com.file_service.shared.application;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TransactionalUseCase {
    int timeout() default -1;
    boolean readOnly() default false;
}
