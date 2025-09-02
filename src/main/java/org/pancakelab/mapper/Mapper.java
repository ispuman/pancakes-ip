package org.pancakelab.mapper;

@FunctionalInterface
public interface Mapper<S, T> {
    T toDTO(S source);
}
