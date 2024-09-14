package com.example.Business.mappers;

public interface Mapper<A, B> {

    B mapTo(A a);
    A mapFrom(B b);
}
