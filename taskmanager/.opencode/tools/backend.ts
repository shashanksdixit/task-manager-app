import { tool } from "@opencode-ai/plugin"
import { execSync } from "node:child_process"

export const test = tool({
  description: "Runs the Spring Boot backend test suite (mvnw test) and returns a summary of pass/fail counts",
  args: {},
  async execute() {
    let output: string
    try {
      output = execSync("mvnw.cmd test -q", {
        encoding: "utf-8",
        cwd: process.cwd(),
        maxBuffer: 1024 * 1024 * 20,
      })
    } catch (err: any) {
      output = (err.stdout?.toString() ?? "") + (err.stderr?.toString() ?? "") || err.message
    }
    const matches = output.match(/Tests run: \d+, Failures: \d+, Errors: \d+/g)
    const last = matches ? matches[matches.length - 1] : `No summary line found. Raw tail:\n${output.slice(-500)}`
    return `Test run complete.\n${last}`
  },
})