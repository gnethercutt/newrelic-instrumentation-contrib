package com.newrelic.agent.instrumentation;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.agent.bridge.Token;
import com.newrelic.agent.bridge.Transaction;
import com.newrelic.api.agent.Logger;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import java.io.PrintStream;
import java.util.logging.Level;

@Weave(originalName="com.lambdaworks.redis.protocol.RedisCommand", type=MatchType.Interface)
public class RedisCommand_Instrumentation {
    @NewField
    protected Token nrToken;

    public RedisCommand_Instrumentation() {
        if (AgentBridge.getAgent().getTransaction().isStarted()) {
            NewRelic.getAgent().getLogger().log(Level.FINER, "RedisCommand created on thread " + Thread.currentThread().getName(), new Object[0]);
            this.nrToken = AgentBridge.getAgent().getTransaction().getToken();
        }
    }

    @Trace(async=true)
    public void complete() {
        NewRelic.getAgent().getLogger().log(Level.FINER, "RedisCommand.complete invoked on thread " + Thread.currentThread().getName(), new Object[0]);
        if (this.nrToken != null) {
            this.nrToken.link();
        }
        Weaver.callOriginal();
        if (this.nrToken != null) {
            this.nrToken.expire();
        }
    }
}
