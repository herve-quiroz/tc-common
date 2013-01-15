/*
 * Copyright (C) 2013 Herve Quiroz
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 */
package org.trancecode.common.system;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.trancecode.collection.TcLists;
import org.trancecode.common.error.FatalError;
import org.trancecode.io.TcByteStreams;

/**
 * Utility methods to deal with processes.
 * 
 * @author Herve quiroz
 */
public final class TcProcesses
{
    private TcProcesses()
    {
        // No instantiation
    }

    public static final class Builder
    {
        private final String command;
        private final List<String> args = Lists.newArrayList();
        private InputStream stdin = System.in;
        private OutputStream stdout = System.out;
        private OutputStream stderr = System.err;

        private Builder(final String command)
        {
            this.command = Preconditions.checkNotNull(command);
        }

        public Builder args(final String... args)
        {
            this.args.addAll(ImmutableList.copyOf(args));
            return this;
        }

        public Builder stdin(final InputStream stdin)
        {
            this.stdin = stdin;
            return this;
        }

        public Builder stdout(final OutputStream stdout)
        {
            this.stdout = stdout;
            return this;
        }

        public Builder stderr(final OutputStream stderr)
        {
            this.stderr = stderr;
            return this;
        }

        public Future<Integer> execute()
        {
            final List<String> commandLine = TcLists.immutableList(command, args);
            final ProcessBuilder processBuilder = new ProcessBuilder(commandLine);
            final Process process;
            try
            {
                process = processBuilder.start();
            }
            catch (final IOException e)
            {
                throw new IllegalStateException(e);
            }
            TcByteStreams.concurrentCopy(process.getInputStream(), stdout, false);
            TcByteStreams.concurrentCopy(process.getErrorStream(), stderr, false);
            TcByteStreams.concurrentCopy(stdin, process.getOutputStream(), false);
            return new Future<Integer>()
            {
                private boolean cancelled;
                private boolean done;
                private int exitValue = -1;

                @Override
                public synchronized boolean cancel(final boolean mayInterruptIfRunning)
                {
                    if (cancelled)
                    {
                        return true;
                    }

                    if (done)
                    {
                        return false;
                    }

                    process.destroy();
                    // TODO force kill if this does not work
                    cancelled = true;
                    return true;
                }

                @Override
                public boolean isCancelled()
                {
                    return cancelled;
                }

                @Override
                public synchronized boolean isDone()
                {
                    if (done)
                    {
                        return true;
                    }

                    if (isCancelled())
                    {
                        done = true;
                        return true;
                    }

                    try
                    {
                        exitValue = process.exitValue();
                        done = true;
                        return true;
                    }
                    catch (final IllegalThreadStateException e)
                    {
                        return false;
                    }
                }

                @Override
                public Integer get() throws InterruptedException, ExecutionException
                {
                    process.waitFor();
                    try
                    {
                        return get(1, TimeUnit.SECONDS);
                    }
                    catch (final TimeoutException e)
                    {
                        throw new FatalError("timeout exceeded!");
                    }
                }

                @Override
                public synchronized Integer get(final long timeout, final TimeUnit unit) throws InterruptedException,
                        ExecutionException, TimeoutException
                {
                    final long timeoutMillis = unit.toMillis(timeout);
                    long sleepDuration = 1;
                    for (long duration = 1; duration < timeoutMillis; duration += sleepDuration)
                    {
                        if (isDone() || isCancelled())
                        {
                            exitValue = process.exitValue();
                            return exitValue;
                        }
                        sleepDuration = Math.min(duration, TimeUnit.SECONDS.toMillis(1));
                        Thread.sleep(sleepDuration);
                    }
                    throw new TimeoutException(String.format("%s %s", timeout, unit));
                }
            };
        }
    }

    public static Builder newProcess(final String command)
    {
        return new Builder(command);
    }
}
