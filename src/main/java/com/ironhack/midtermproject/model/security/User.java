package com.ironhack.midtermproject.model.security;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Inheritance
public class User {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  protected Integer id;
  protected String username;
  protected String password;

  @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="user")
  protected Set<Role> roles = new HashSet<>();

  public User() {
  }

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  @JsonIgnore
  public Set<Role> getRoles() {
    return roles;
  }
  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  @Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", authorities=" + roles +
            '}';
  }
}
