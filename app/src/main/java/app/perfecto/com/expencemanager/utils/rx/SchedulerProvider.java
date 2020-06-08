package app.perfecto.com.expencemanager.utils.rx;

import io.reactivex.Scheduler;


public interface SchedulerProvider {

    Scheduler ui();

    Scheduler computation();

    Scheduler io();

}
