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
package org.trancecode.common.jvm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.Future;

import org.trancecode.common.system.TcProcesses;

/**
 * Utility methods to run Java processes.
 * 
 * @author Herve Quiroz
 */
public final class JavaProcesses
{
    private JavaProcesses()
    {
        // No instantiation
    }

    public static final class Builder
    {
        private final List<String> jvmOptions = Lists.newArrayList();
        private String jarFile;
        private final List<String> args = Lists.newArrayList();
        private final TcProcesses.Builder processBuilder;

        private Builder(final String javaCommand)
        {
            processBuilder = TcProcesses.newProcess(javaCommand);
        }

        public Builder jarFile(final String jarFile)
        {
            this.jarFile = jarFile;
            return this;
        }

        public Builder jvmOptions(final String... jvmOptions)
        {
            this.jvmOptions.addAll(ImmutableList.copyOf(jvmOptions));
            return this;
        }

        public Builder args(final String... args)
        {
            this.args.addAll(ImmutableList.copyOf(args));
            return this;
        }

        public Builder stdin(final InputStream stdin)
        {
            processBuilder.stdin(stdin);
            return this;
        }

        public Builder stdout(final OutputStream stdout)
        {
            processBuilder.stdout(stdout);
            return this;
        }

        public Builder stderr(final OutputStream stderr)
        {
            processBuilder.stderr(stderr);
            return this;
        }

        public Future<Integer> execute()
        {
            processBuilder.args(jvmOptions);
            if (jarFile != null)
            {
                processBuilder.args("-jar", jarFile);
            }
            else
            {
                throw new UnsupportedOperationException("TODO");
            }
            processBuilder.args(args);
            return processBuilder.execute();
        }
    }

    public static Builder newJavaProcess()
    {
        return newJavaProcess(Jvm.getCommand());
    }

    public static Builder newJavaProcess(final String javaCommand)
    {
        return new Builder(javaCommand);
    }
}
