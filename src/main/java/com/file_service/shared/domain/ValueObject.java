package com.file_service.shared.domain;

import java.io.Serializable;
public interface ValueObject extends Serializable {
    default void validate() {}
}


