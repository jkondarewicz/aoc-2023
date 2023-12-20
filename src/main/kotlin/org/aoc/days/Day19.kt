package org.aoc.days

import org.aoc.data.AoC
import java.lang.Error

class Day19 : AoC<Day19.Workflow, Long, Long>() {
    override fun part1(parsedData: Workflow): Long {
        val rules = parsedData.rules
        return parsedData.data.filter { workflow ->
            var rule = rules["in"]!!
            var valid = false
            while (true) {
                val goto = rule.firstNotNullOf { it.goto(workflow) }
                if (goto == "A") {
                    valid = true
                    break
                } else if (goto == "R") {
                    valid = false
                    break
                } else {
                    rule = rules[goto]!!
                }
            }
            valid
        }.sumOf { it.sum() }
    }

    override fun part2(parsedData: Workflow): Long {
        val possibilities = RulePossibilities()
        return process(possibilities, parsedData, parsedData.rules["in"]!!).toSet().sumOf { it.countPossibilities() }
    }

    private fun process(possibilities: RulePossibilities, workflow: Workflow, rules: List<WorkflowRule>): List<RulePossibilities> {
        var np = possibilities
        return rules.flatMap { rule ->
            val cp = rule.possibilities(np, false)
            np = rule.possibilities(np, true)
            if ("R".contains(rule.goto)) listOf()
            else if ("A".contains(rule.goto)) if (cp.valid()) listOf(cp) else listOf()
            else process(cp, workflow, workflow.rules[rule.goto]!!)
        }
    }

    override fun parseInput(input: String, step: Int): Workflow {
        val rules = mutableMapOf<String, MutableList<WorkflowRule>>()
        val workflowData = mutableListOf<WorfkflowData>()
        input.split("\n").forEach { line ->
            if (line.startsWith("{")) {
                val nums = line.split("{")[1].split("}")[0].split(",")
                workflowData.add(WorfkflowData().apply { put(nums[0].getNum(), nums[1].getNum(), nums[2].getNum(), nums[3].getNum()) })
            } else if (line.isNotEmpty()) {
                val l = line.split("{")
                val box = l[0]
                val data = l[1].split("}")[0].split(",")
                data.forEach { d ->
                    if (!d.contains(":")) {
                        rules.getOrPut(box) { mutableListOf() }.add(WorkflowRule(d))
                    } else {
                        val str = d.split(":")
                        val goto = str[1]
                        val bigger = str[0][1] == '>'
                        val c = str[0][0]
                        val num = str[0].substring(2, str[0].length).toLong()
                        rules.getOrPut(box) { mutableListOf() }.add(WorkflowRule(c, bigger, num, goto))
                    }
                }
            }
        }
        return Workflow(rules, workflowData)
    }

    private fun String.getNum() = split("=")[1].toLong()

    data class RulePossibilities(
        val x: Rule = Rule(),
        val m: Rule = Rule(),
        val a: Rule = Rule(),
        val s: Rule = Rule()
    ) {
        fun valid() = x.valid() && m.valid() && a.valid() && s.valid()
        fun countPossibilities(): Long = x.countPossibilities() * m.countPossibilities() * a.countPossibilities() * s.countPossibilities()
    }

    data class Rule(
        val min: Long,
        val max: Long
    ) {
        fun valid() =
            min <= max
        fun calculate(num: Long, bigger: Boolean, inv: Boolean): Rule {
            var min = this.min
            var max = this.max
            if (bigger) {
                min = num + if (inv) 0 else 1
            } else {
                max = num - if (inv) 0 else 1
            }
            return Rule(min, max)
        }

        fun countPossibilities(): Long {
            return max - min + 1
        }

        constructor(): this(1L, 4000L)
    }
    data class WorfkflowData(
        val data: MutableMap<Char, Long> = mutableMapOf()
    ) {

        fun put(x: Long, m: Long, a: Long, s: Long) {
            data['x'] = x
            data['m'] = m
            data['a'] = a
            data['s'] = s
        }

        fun sum(): Long =
            data['x']!! + data['m']!! + data['s']!! + data['a']!!
    }

    data class WorkflowRule(
        val category: Char?,
        val bigger: Boolean?,
        val num: Long?,
        val goto: String
    ) {
        constructor(goto: String): this(null, null, null, goto)

        fun possibilities(rulePossibilities: RulePossibilities, inv: Boolean = false): RulePossibilities {
            return if (category != null && bigger != null && num != null) {
                val b = if (inv) !bigger else bigger
                 when (category) {
                    'x' -> rulePossibilities.copy(x = rulePossibilities.x.calculate(num, b, inv))
                    'm' -> rulePossibilities.copy(m = rulePossibilities.m.calculate(num, b, inv))
                    'a' -> rulePossibilities.copy(a = rulePossibilities.a.calculate(num, b, inv))
                    's' -> rulePossibilities.copy(s = rulePossibilities.s.calculate(num, b, inv))
                     else -> throw Error()
                }
            } else {
                rulePossibilities.copy()
            }
        }


        fun goto(workflow: WorfkflowData): String? {
            return if (category != null && bigger != null && num != null) {
                val value = workflow.data[category]!!
                if ((bigger && value > num) || (!bigger && value < num)) goto
                else null
            } else {
                goto
            }
        }

    }

    data class Workflow(
        val rules: Map<String, List<WorkflowRule>>,
        val data: List<WorfkflowData>
    )
}