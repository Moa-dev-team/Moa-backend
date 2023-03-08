package com.moa.domain.study.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
public class Study {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  @Column(name = "introduce_content", length = 500)
  private String introduceContent;

  @Column(name = "requirement_content", length = 500)
  private String requirementContent;

  @Column(name = "recruit_number")
  private int recruitNumber;

  @Column(name = "current_number")
  private int currentNumber;

  @Column(name = "proceed_way")
  private String proceedWay;

  @Temporal(TemporalType.TIMESTAMP)
  @UpdateTimestamp // 혹시 몰라서 넣어 놓음
  @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", name = "register_date")
  private Date registerDate;

  @Temporal(TemporalType.TIMESTAMP)
  @UpdateTimestamp
  @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", name = "update_date")
  private Date updateDate;

  @Column(name = "register_date") // 시작 예정일을 비워둘 수 있게?
  private Date startDate;

  private int period;

  @Column(name = "visit_count")
  @ColumnDefault("0")  // 조회수 0 시작이니까 디폴트 적용
  private int visitCount;

  @ColumnDefault("0") // 생성 할 땐 모집 중이므로 0을 디폴트로
  private int status;
}
