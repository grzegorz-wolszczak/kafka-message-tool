package application.kafka.dto;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/*
        // udpatedable topic configs

    log.segment.bytes
    log.roll.ms
    log.roll.hours
    log.roll.jitter.ms
    log.roll.jitter.hours
    log.index.size.max.bytes
    log.flush.interval.messages
    log.flush.interval.ms
    log.retention.bytes
    log.retention.ms
    log.retention.minutes
    log.retention.hours
    log.index.interval.bytes
    log.cleaner.delete.retention.ms
    log.cleaner.min.compaction.lag.ms
    log.cleaner.min.cleanable.ratio
    log.cleanup.policy
    log.segment.delete.delay.ms
    unclean.leader.election.enable
    min.insync.replicas
    max.message.bytes
    compression.type
    log.preallocate
    log.message.timestamp.type
    log.message.timestamp.difference.max.ms

 */
public class TopicAlterableProperties {

    private StringProperty topicName = new SimpleStringProperty("test");
    private IntegerProperty retentionMilliseconds = new SimpleIntegerProperty(1);

    public TopicAlterableProperties(String topicName) {
        this.topicName.set(topicName);
    }

    public StringProperty topicNameProperty() {
        return topicName;
    }

    public int getRetentionMilliseconds() {
        return retentionMilliseconds.get();
    }

    public void setRetentionMilliseconds(int retentionMilliseconds) {
        this.retentionMilliseconds.set(retentionMilliseconds);
    }

    public IntegerProperty retentionMillisecondsProperty() {
        return retentionMilliseconds;
    }

    public String getTopicName() {
        return topicName.get();
    }

    public void setTopicName(String topicName) {
        this.topicName.set(topicName);
    }


}
