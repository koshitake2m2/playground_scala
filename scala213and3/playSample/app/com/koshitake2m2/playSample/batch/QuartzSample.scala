package com.koshitake2m2.playSample.batch

import org.quartz.CronScheduleBuilder.cronSchedule
import org.quartz.JobBuilder.newJob
import org.quartz.Scheduler
import org.quartz.TriggerBuilder.newTrigger
import org.quartz.impl.StdSchedulerFactory
import org.slf4j.Logger

import javax.inject.Inject

// ref: https://github.com/quartz-scheduler/quartz/blob/main/examples/src/main/java/org/quartz/examples/example3/SimpleJob.java
case class MyJob() extends org.quartz.Job {
  override def execute(context: org.quartz.JobExecutionContext): Unit =
    println("Hello Quartz!")
}

// ref: https://github.com/quartz-scheduler/quartz/blob/main/examples/src/main/java/org/quartz/examples/example3/CronTriggerExample.java
class QuartzSample @Inject() (
    logger: Logger
) {
  val sf = new StdSchedulerFactory()
  val sched: Scheduler = sf.getScheduler

  println("##################################")
  logger.info("##################################")

  val job = newJob(classOf[MyJob]).withIdentity("job1", "group1").build

  // 2秒ごとに実行
  val trigger = newTrigger.withIdentity("trigger1", "group1").withSchedule(cronSchedule("0/2 * * * * ?")).build

  sched.scheduleJob(job, trigger)
  sched.start()

  // 10秒後にシャットダウン
  Thread.sleep(10 * 1000)
  sched.shutdown(true)
}
