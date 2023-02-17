package com.example.accountx.command;

/**
 * For undo operations...
 * @param <T>
 */
public interface ICommand<T>
{
    T apply();
    T undo();
}
