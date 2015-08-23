package dao;


import java.util.List;

public interface CommonDAO {
    <T> List<T> getAll(Class<T> clazz);
    <T> T save(T t);
    <T> void delete(T t);
}
