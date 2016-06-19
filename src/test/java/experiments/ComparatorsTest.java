package experiments;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class ComparatorsTest {

  private List<Person> group;

  @Before
  public void setUp() throws Exception {
    group = Arrays.asList(
        new Person("Ram", 20),
        new Person("Shyam", 40),
        new Person("Sita", 30),
        new Person("Gita", 40)
    );
  }

  @Test
  public void shouldCompareByNameAscending() throws Exception {
    Comparator<Person> comparator = comparing(Person::getName);

    assertThat(groupSortedBy(comparator))
        .extracting(Person::getName, Person::getAge)
        .containsExactly(
            tuple("Gita", 40),
            tuple("Ram", 20),
            tuple("Shyam", 40),
            tuple("Sita", 30)
        );
  }

  @Test
  public void shouldCompareByAgeAscending() throws Exception {
    Comparator<Person> comparator = comparingInt(Person::getAge);

    assertThat(groupSortedBy(comparator))
        .extracting(Person::getName, Person::getAge)
        .containsExactly(
            tuple("Ram", 20),
            tuple("Sita", 30),
            tuple("Shyam", 40),
            tuple("Gita", 40)
        );
  }

  @Test
  public void shouldCompareByNameDescending() throws Exception {
    Comparator<Person> comparator =
        comparing(Person::getName, reverseOrder());

    assertThat(groupSortedBy(comparator))
        .extracting(Person::getName, Person::getAge)
        .containsExactly(
            tuple("Sita", 30),
            tuple("Shyam", 40),
            tuple("Ram", 20),
            tuple("Gita", 40)
        );
  }

  @Test
  public void shouldCompareByNameAndAge() throws Exception {
    Comparator<Person> comparator = comparingInt(Person::getAge)
        .thenComparing(Person::getName);

    assertThat(groupSortedBy(comparator))
        .extracting(Person::getName, Person::getAge)
        .containsExactly(
            tuple("Ram", 20),
            tuple("Sita", 30),
            tuple("Gita", 40),
            tuple("Shyam", 40)
        );
  }

  @Test
  public void shouldHandleNullValues() throws Exception {
    List<Person> group = Arrays.asList(
        new Person(null, 12),
        new Person("Sita", 30)
    );

    Comparator<Person> comparing = comparing(Person::getName, nullsLast(naturalOrder()));

    List<Person> sorted = group.stream().sorted(comparing).collect(toList());

    assertThat(sorted)
        .extracting(Person::getName, Person::getAge)
        .containsExactly(
            tuple("Sita", 30),
            tuple(null, 12)
        );
  }

  private List<Person> groupSortedBy(Comparator<Person> comparator) {
    return group.stream()
        .sorted(comparator)
        .collect(toList());
  }


  private class Person {
    private final String name;
    private final int age;

    private Person(String name, int age) {
      this.name = name;
      this.age = age;
    }

    String getName() {
      return name;
    }

    int getAge() {
      return age;
    }
  }
}
