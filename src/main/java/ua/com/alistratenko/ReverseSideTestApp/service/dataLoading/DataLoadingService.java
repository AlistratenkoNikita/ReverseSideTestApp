package ua.com.alistratenko.ReverseSideTestApp.service.dataLoading;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ua.com.alistratenko.ReverseSideTestApp.dal.GeneralDAL;
import ua.com.alistratenko.ReverseSideTestApp.requestSender.RequestSender;

/**
 * Class that provides data loading service for specific info
 *
 * @author Nikita
 * @since 09.09.2018
 */
public abstract class DataLoadingService {

    @Value("${application.vk.groupid}")
    protected int GROUP_ID;

    @Value("${pause-for-every-request}")
    protected int PAUSE_FOR_EVERY_REQUEST;

    @Autowired
    protected GeneralDAL generalDAL;

    @Autowired
    protected RequestSender requestSender;

    @Autowired
    protected Logger logger;

    /**
     * Loads all needed data
     */
    public abstract void loadData();
}
