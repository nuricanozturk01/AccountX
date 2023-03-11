package com.example.accountx.command;

/**
 * For undo operations...
 * @param
 */
@SuppressWarnings("all")
public interface ICommand<T>
{
   T applyAdd(T item);
   T applyRemove(T item);
   T applyUpdate(T item);
   T undo();
}
