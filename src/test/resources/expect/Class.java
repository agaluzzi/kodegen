/*
Please do not modify this file.
This is generated code.
*/

package com.example;

import java.io.Serializable;
import java.util.Objects;


/**
 * A human being.
 */
public class Person implements Serializable
{
    public static final Person JOHN_DOE = new Person("John", "Doe", 999);
    private final String first;
    private final String last;
    private final long timeOfBirth;
    protected volatile int age;

    /**
     * Constructs a new Person.
     *
     * @param first The given name
     * @param last The family name
     * @param age The number of years lived
     */
    public Person(String first, String last, long timeOfBirth, int age)
    {
        Objects.requireNonNull(first, "first");
        Objects.requireNonNull(last, "last");
        this.first = first;
        this.last = last;
        this.timeOfBirth = timeOfBirth;
        this.age = age;
    }

    /**
     * @param first The given name
     * @param last The family name
     */
    protected Person(String first, String last)
    {
        this.first = first.trim();
        this.last = last.trim();
    }

    /**
     * @return The given name
     */
    public String getFirst()
    {
        return first;
    }

    /**
     * @return The family name
     */
    public String getLast()
    {
        return last;
    }

    public long getTimeOfBirth()
    {
        return timeOfBirth;
    }

    /**
     * @return The number of years lived
     */
    public int getAge()
    {
        return age;
    }

    /**
     * @param age The number of years lived
     */
    public void setAge(int age)
    {
        this.age = age;
    }

    /**
     * Adds another year lived.
     * <p>
     * This should only be invoked annually.
     */
    public synchronized void happyBirthday()
    {
        this.age++;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof Person)) return false;
        final Person other = (Person) o;
        if (!Objects.equals(first, other.first)) return false;
        if (!Objects.equals(last, other.last)) return false;
        if (timeOfBirth != other.timeOfBirth) return false;
        if (age != other.age) return false;
        return true;
    }

    @Override
    public int hashCode()
    {
        int result = 0;
        result = 31 * result + (first != null ? first.hashCode() : 0);
        result = 31 * result + (last != null ? last.hashCode() : 0);
        result = 31 * result + (int) (timeOfBirth ^ (timeOfBirth >>> 32));
        result = 31 * result + age;
        return result;
    }

    @Override
    public String toString()
    {
        return "Person[" +
               "first=" + this.first +
               ", last=" + this.last +
               ", timeOfBirth=" + this.timeOfBirth +
               ", age=" + this.age +
               "]";
    }
}