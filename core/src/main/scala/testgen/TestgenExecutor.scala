/*
 * Copyright 2011 Kazuhiro SERA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package testgen

class TestgenExecutor(val config: Config) {

  def generateTests(pathOrPackage: String): Seq[Test] = {
    val targets = new TargetExtractor(config).extract(pathOrPackage)
    val generator = new TestGenerator(config)
    targets match {
      case Nil => Nil
      case allTargets => allTargets map (target => generator.generate(target))
    }
  }

}
