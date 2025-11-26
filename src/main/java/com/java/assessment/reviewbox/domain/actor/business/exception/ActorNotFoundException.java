package com.java.assessment.reviewbox.domain.actor.business.exception;

import com.java.assessment.reviewbox.domain.actor.business.exception.code.ActorErrorCode;
import com.java.assessment.reviewbox.global.exception.BusinessException;

public class ActorNotFoundException extends BusinessException {
    public static final ActorNotFoundException EXCEPTION = new ActorNotFoundException();

    private ActorNotFoundException() {
        super(ActorErrorCode.ACTOR_NOT_FOUND);
    }
}
