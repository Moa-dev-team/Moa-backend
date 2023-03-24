package com.moa.domain.study.entity;

import com.moa.domain.utility.BaseEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
public class Study extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "title")
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

  @Column(name = "period")
  private int period;

  // 디폴트 적용시킬 수 있는 방법 찾아보기
  // Prepersist(null일때만 디폴트 적용시키고 싶을 때 )
  // 또는 @Column에 columnDefinition = "자료형 default ''" 추가
  @Column(name = "visit_count")
  private int visitCount;

  @Column(name = "status")
  private int status;

}


